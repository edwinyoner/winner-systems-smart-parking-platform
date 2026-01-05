import { ApplicationConfig, importProvidersFrom } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {
  provideRouter,
  withEnabledBlockingInitialNavigation,
  withHashLocation,
  withInMemoryScrolling,
  withRouterConfig,
  withViewTransitions
} from '@angular/router';

// ✅ AGREGAR ESTE IMPORT
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { DropdownModule, SidebarModule } from '@coreui/angular';
import { IconSetService } from '@coreui/icons-angular';

// Font Awesome Module
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { routes } from './app.routes';

// Importar interceptors
import { loadingInterceptor } from './core/interceptors/loading.interceptor';
import { authInterceptor } from './core/interceptors/auth.interceptor';
import { errorInterceptor } from './core/interceptors/error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes,
      withRouterConfig({
        onSameUrlNavigation: 'reload'
      }),
      withInMemoryScrolling({
        scrollPositionRestoration: 'top',
        anchorScrolling: 'enabled'
      }),
      withEnabledBlockingInitialNavigation(),
      withViewTransitions(),
      //withHashLocation() // URL con hash (#)
    ),
    // Module Providers
    importProvidersFrom(
      SidebarModule, 
      DropdownModule,
      FontAwesomeModule  // Font Awesome Module
    ),
    IconSetService,
    provideAnimationsAsync(),

    // ✅ Configurar HttpClient con interceptors
    provideHttpClient(
      withInterceptors([
        loadingInterceptor,  // 1. Primero loading (empieza a contar)
        authInterceptor,     // 2. Luego agregar token
        errorInterceptor     // 3. Finalmente manejar errores
      ])
    )
  ]
};

