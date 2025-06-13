import {ActivatedRouteSnapshot, CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from './services/auth.service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  if (!auth.isLoggedIn()) {
    router.navigate(['/login']);
    return false;
  }

  const requiredRoles = route.data?.['roles'] as string[] | undefined;
  const hasRole = !requiredRoles || requiredRoles.some(role => auth.getUserRoles().includes(role));

  if (!hasRole) {
    return router.parseUrl('/login');
  }
  return true;
};
