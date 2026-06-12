export function canManageRoles(user) {
    return (user?.roles || []).some((role) => role === 'ADMIN' || role === 'ORGANIZER');
}

export function isAdmin(user) {
    return (user?.roles || []).includes('ADMIN');
}

export function hasRole(user, role) {
    return (user?.roles || []).includes(role);
}
