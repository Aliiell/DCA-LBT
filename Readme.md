Aplicación Local Bug Tracker

##Funcionalidades##

Esta es mi aplicación LBT desarrollada en Springboot donde incluye las siguientes funcionalidades:

-Registrar un usuario
-Iniciar sesión con un usuario (Viene ya en el sistema usuarios predeterminados como son usuario admin con una contraseña admin, usuario1 con contraseña usuario1, usuario2 con contraseña usuario2, usuario3 con contraseña usuario3).
-Una vez iniciado sesión se puede ver un listado de los issues que hay.
-Abrir un Issue donde se debe rellenar el nombre, descripcion y si se desea se pueden añadir etiquetas separadas con comas(tag1, tag2, tag3...), y puede ser realizado tanto por admin como por clientes normales.
-Dentro de un Issue se pueden añadir comentarios y ver los comentarios de los demás y esto lo puede realizar tanto admin como cliente normal.
-Solo admin puede cerrar y reabrir issues, y si un issue está cerrado no es posible comentar, a no ser que el admin decida reabrir ese issue.


Respecto a la característica adicional, he implementado la posibilidad de añadir etiquetas para etiquetar los issues, poder poner etiquetas como grave, leve, etc., ya que considero que es una caracteristica que está prácticamente en todos los sistemas de seguimiento de fallos.

##Funcionamiento##

