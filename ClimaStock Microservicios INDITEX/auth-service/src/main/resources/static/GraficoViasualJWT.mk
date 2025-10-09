[Cliente]
   |
   | 1️⃣ Envía usuario + contraseña al endpoint /auth/login
   v
[AuthController]
   |
   | 2️⃣ Llama a JwtUtil para generar un token
   v
[JwtUtil] -----> crea JWT firmado con clave secreta
   |
   | 3️⃣ Devuelve el token al cliente
   |
   v
==========================
↓ A partir de aquí, cada petición incluye el token ↓
==========================

[Cliente]
   |
   | 4️⃣ Envía petición con el token (en el header)
   v
[JwtFilter]
   |
   | 5️⃣ Llama a JwtUtil para validar el token
   v
[JwtUtil] -----> revisa firma, fecha y usuario
   |
   | 6️⃣ Si es válido → JwtFilter le pasa el usuario a Spring Security
   v
[SecurityConfig] → deja pasar la petición (si las reglas lo permiten)
   |
   v
[Controlador protegido] → procesa la solicitud
