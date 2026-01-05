
SELECT * FROM users;
SELECT * FROM roles;
SELECT * FROM permissions;

SELECT * FROM users;
"id"	"created_at"	"created_by"	"deleted_at"	"deleted_by"	"email"	"email_verified"	"first_name"	"last_name"	"password"	"phone_number"	"profile_picture"	"status"	"updated_at"	"updated_by"
1	"2025-12-06 21:18:16.766344"				"admin@smartparking.com"	true	"Admin"	"System"	"$2a$10$EClvRPpQ7Uo.WXnWt0qfquxOErT82y..AYDabrPmjApmKGe4HXCaG"	"+51987654321"		true	"2025-12-06 21:18:16.766352"	

SELECT * FROM roles;
"id"	"created_at"	"created_by"	"deleted_at"	"deleted_by"	"description"	"name"	"status"	"updated_at"	"updated_by"
1	"2025-12-06 21:18:16.537662"				"Acceso completo al sistema Smart Parking"	"ADMIN"	true	"2025-12-06 21:18:16.537672"	
2	"2025-12-06 21:18:16.607911"				"Gestión de zonas, tarifas y reportes municipales"	"AUTORIDAD"	true	"2025-12-06 21:18:16.607917"	
3	"2025-12-06 21:18:16.627827"				"Operación diaria del sistema de estacionamiento"	"OPERADOR"	true	"2025-12-06 21:18:16.627834"	

SELECT * FROM permissions;
"id"	"created_at"	"created_by"	"deleted_at"	"deleted_by"	"description"	"name"	"status"	"updated_at"	"updated_by"
1	"2025-12-06 21:18:16.42757"				"Crear nuevos usuarios en el sistema"	"users.create"	true	"2025-12-06 21:18:16.427592"	
2	"2025-12-06 21:18:16.466542"				"Ver información de usuarios"	"users.read"	true	"2025-12-06 21:18:16.466555"	
3	"2025-12-06 21:18:16.468734"				"Actualizar información de usuarios"	"users.update"	true	"2025-12-06 21:18:16.468747"	
4	"2025-12-06 21:18:16.470563"				"Eliminar usuarios del sistema"	"users.delete"	true	"2025-12-06 21:18:16.470573"	
5	"2025-12-06 21:18:16.472417"				"Restaurar usuarios eliminados"	"users.restore"	true	"2025-12-06 21:18:16.472429"	
6	"2025-12-06 21:18:16.474649"				"Crear nuevos roles"	"roles.create"	true	"2025-12-06 21:18:16.47466"	
7	"2025-12-06 21:18:16.477931"				"Ver información de roles"	"roles.read"	true	"2025-12-06 21:18:16.477942"	
8	"2025-12-06 21:18:16.479669"				"Actualizar roles existentes"	"roles.update"	true	"2025-12-06 21:18:16.479679"	
9	"2025-12-06 21:18:16.481279"				"Eliminar roles del sistema"	"roles.delete"	true	"2025-12-06 21:18:16.481289"	
10	"2025-12-06 21:18:16.483073"				"Crear zonas de estacionamiento"	"parking.create"	true	"2025-12-06 21:18:16.483083"	
11	"2025-12-06 21:18:16.484866"				"Ver zonas de estacionamiento"	"parking.read"	true	"2025-12-06 21:18:16.484877"	
12	"2025-12-06 21:18:16.486737"				"Actualizar zonas de estacionamiento"	"parking.update"	true	"2025-12-06 21:18:16.486748"	
13	"2025-12-06 21:18:16.488546"				"Eliminar zonas de estacionamiento"	"parking.delete"	true	"2025-12-06 21:18:16.488557"	
14	"2025-12-06 21:18:16.490232"				"Crear nuevas tarifas"	"rates.create"	true	"2025-12-06 21:18:16.490242"	
15	"2025-12-06 21:18:16.491808"				"Ver tarifas configuradas"	"rates.read"	true	"2025-12-06 21:18:16.491818"	
16	"2025-12-06 21:18:16.493364"				"Actualizar tarifas existentes"	"rates.update"	true	"2025-12-06 21:18:16.493376"	
17	"2025-12-06 21:18:16.494977"				"Eliminar tarifas"	"rates.delete"	true	"2025-12-06 21:18:16.494991"	
18	"2025-12-06 21:18:16.496646"				"Ver reportes y estadísticas del sistema"	"reports.view"	true	"2025-12-06 21:18:16.496662"	



SELECT 
    u.email,
    r.name as role_name
FROM users u
INNER JOIN user_roles ur ON u.id = ur.user_id
INNER JOIN roles r ON ur.role_id = r.id
WHERE u.email = 'admin@smartparking.com';

"email"	"role_name"
"admin@smartparking.com"	"ADMIN"

SELECT 
    r.name as role_name,
    COUNT(p.id) as total_permissions,
    STRING_AGG(p.name, ', ' ORDER BY p.name) as permissions
FROM roles r
INNER JOIN role_permissions rp ON r.id = rp.role_id
INNER JOIN permissions p ON rp.permission_id = p.id
WHERE r.name = 'ADMIN'
GROUP BY r.name;

"role_name"	"total_permissions"	"permissions"
"ADMIN"	18	"parking.create, parking.delete, parking.read, parking.update, rates.create, rates.delete, rates.read, rates.update, reports.view, roles.create, roles.delete, roles.read, roles.update, users.create, users.delete, users.read, users.restore, users.update"


SELECT 
    r.name as role_name,
    COUNT(p.id) as total_permissions,
    STRING_AGG(p.name, ', ' ORDER BY p.name) as permissions
FROM roles r
INNER JOIN role_permissions rp ON r.id = rp.role_id
INNER JOIN permissions p ON rp.permission_id = p.id
WHERE r.name = 'OPERADOR'
GROUP BY r.name;


