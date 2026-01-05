import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermissionSelectorComponent } from './permission-selector.component';

describe('PermissionSelectorComponent', () => {
  let component: PermissionSelectorComponent;
  let fixture: ComponentFixture<PermissionSelectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PermissionSelectorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PermissionSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
