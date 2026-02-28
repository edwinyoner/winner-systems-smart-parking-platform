import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormBuilder, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { debounceTime, distinctUntilChanged } from "rxjs/operators";

// Services
import { UserService } from "../../../core/services/user.service";
import { RoleService } from "../../../core/services/role.service";
import { AuthContextService } from "../../../core/services/auth-context.service";

// Models
import { User, UserFilters } from "../../../core/models/user.model";
import { Role } from "../../../core/models/role.model";
import { PaginatedResponse } from "../../../core/models/pagination.model";

// Components
import { AlertMessageComponent } from "../../../shared/components/alert-message/alert-message.component";

@Component({
  selector: "app-user-list",
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    AlertMessageComponent
  ],
  templateUrl: "./user-list.component.html",
  styleUrls: ["./user-list.component.scss"],
})
export class UserListComponent implements OnInit {
  filterForm!: FormGroup;
  users: User[] = [];
  roles: Role[] = [];
  isLoading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  // Paginación
  currentPage = 1;
  pageSize = 10;
  totalItems = 0;
  totalPages = 0;
  pageSizeOptions = [5, 10, 15, 20, 25, 50];

  // Confirmación de eliminación
  showDeleteConfirm = false;
  userToDelete: User | null = null;

  // Math para el template
  Math = Math;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private roleService: RoleService,
    private router: Router,
    private authContext: AuthContextService 
  ) {
    // Capturar mensajes de éxito desde la navegación
    const state = window.history.state;
    if (state?.["successMessage"]) {
      this.successMessage = state["successMessage"];
      setTimeout(() => (this.successMessage = null), 5000);
    }
  }

  ngOnInit(): void {
    this.initFilterForm();
    this.loadRoles();
    this.loadUsers();
    this.setupFilterListeners();
  }

  /**
   * MODO DE USO EN HTML: @if (hasPermission('users.create'))
   */
  hasPermission(permission: string): boolean {
    return this.authContext.hasPermission(permission);
  }

  private initFilterForm(): void {
    this.filterForm = this.fb.group({
      search: [""],
      roleId: [""],
      status: [""],
      emailVerified: [""],
    });
  }

  private setupFilterListeners(): void {
    // Búsqueda con debounce
    this.filterForm.get("search")?.valueChanges
      .pipe(debounceTime(400), distinctUntilChanged())
      .subscribe(() => {
        this.currentPage = 1;
        this.loadUsers();
      });

    // Otros filtros (Rol, Estado, Verificación)
    ["roleId", "status", "emailVerified"].forEach(key => {
      this.filterForm.get(key)?.valueChanges.subscribe(() => {
        this.currentPage = 1;
        this.loadUsers();
      });
    });
  }

  private loadRoles(): void {
    this.roleService.getAllRoles().subscribe({
      next: (roles) => (this.roles = roles),
      error: (err) => console.error("Error al cargar roles:", err)
    });
  }

  loadUsers(): void {
    this.isLoading = true;
    const val = this.filterForm.value;

    const filters: UserFilters = {
      search: val.search || undefined,
      roleId: val.roleId ? Number(val.roleId) : undefined,
      status: val.status !== "" ? val.status === "true" : undefined,
      emailVerified: val.emailVerified !== "" ? val.emailVerified === "true" : undefined,
      page: this.currentPage - 1,
      size: this.pageSize
    };

    this.userService.getUsers(filters).subscribe({
      next: (res: PaginatedResponse<User>) => {
        this.users = res.content;
        this.totalItems = res.totalElements;
        this.totalPages = res.totalPages;
        this.currentPage = res.number + 1;
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = "Error al cargar los usuarios del sistema";
        console.error(err);
      }
    });
  }

  clearFilters(): void {
    this.filterForm.reset({
      search: "",
      roleId: "",
      status: "",
      emailVerified: ""
    }, { emitEvent: false }); // Evita disparar múltiples llamadas
    this.currentPage = 1;
    this.loadUsers();
  }

  // --- Navegación y Eventos ---

  onPageSizeChange(event: any): void {
    this.pageSize = Number(event.target.value);
    this.currentPage = 1;
    this.loadUsers();
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadUsers();
    }
  }

  getPages(): number[] {
    const pages = [];
    const maxVisiblePages = 5;
    let startPage = Math.max(1, this.currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = Math.min(this.totalPages, startPage + maxVisiblePages - 1);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }

  createUser(): void { this.router.navigate(["/users/create"]); }
  viewUser(id: number): void { this.router.navigate(["/users", id]); }
  editUser(id: number): void { this.router.navigate(["/users", id, "edit"]); }

  confirmDelete(user: User): void {
    this.userToDelete = user;
    this.showDeleteConfirm = true;
  }

  cancelDelete(): void {
    this.showDeleteConfirm = false;
    this.userToDelete = null;
  }

  deleteUser(): void {
    if (!this.userToDelete) return;
    this.isLoading = true;
    
    this.userService.deleteUser(this.userToDelete.id).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = `Usuario "${this.userToDelete?.firstName}" eliminado exitosamente`;
        this.showDeleteConfirm = false;
        this.userToDelete = null;
        this.loadUsers();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || "No se pudo eliminar el usuario";
        this.showDeleteConfirm = false;
      }
    });
  }

  // --- Formateadores ---

  getUserInitials(user: User): string {
    const f = user.firstName?.charAt(0) || '';
    const l = user.lastName?.charAt(0) || '';
    return (f + l).toUpperCase();
  }

  formatDate(date: string | Date): string {
    if (!date) return 'N/A';
    return new Date(date).toLocaleDateString("es-PE", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    });
  }

  dismissSuccess(): void { this.successMessage = null; }
  dismissError(): void { this.errorMessage = null; }
}