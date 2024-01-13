
# Wiki Tree Builder
Este programa permite la creación de un árbol de directorios con HTML para utilizar con la librería de jsTree e iconos de Font Awesome, Glyphicons o iconos personalizados.

## Requisitos previos
Java 1.8 o superior instalado
## Descarga
Solo tienes que descargar la última versión estable del programa en la zona de *Releases* de Github o bien escogiendo la última versión en el [este enlace](https://github.com/pmkirsten/wiki-tree-builder/releases/latest). 
Descargamos el archivo con extensión **\*.jar** y lo ejecutamos. No es necesario instalar.

## Uso
Únicamente es necesario pulsar sobre el botón **Selecciona carpeta...** para que nos abra una venta que nos permita seleccionar la carpeta a analizar.

![Imagen1](https://i.imgur.com/eTU9Lin.png)
![Imagen2](https://i.imgur.com/lNPr7s0.png)
![Imagen3](https://i.imgur.com/U73GBzq.png)

## Changelog
### Versión 1.8.0
- Añadidas git actions
### Versión 1.7.0
- Cambiado el nombre del proyecto
- Añadidos iconos personalizados
- Eliminado envoltorio personalizado del árbol
### Versión 1.6.0
- Se puede marcar / desmarcar un nodo del árbol como seleccionado /deseleccionado , pulsando la tecla 'S'. Esto añadirá / eliminará el atributo *"selected": true* al nodo de un fichero (Funciona también con nodos organizativos)
### Versión 1.5.0
- Se puede marcar / desmarcar un nodo del árbol como seleccionado / deseleccionado , pulsando la tecla 'S'. Esto añadirá / eliminará el atributo *"selected": true* al nodo de un fichero (sólo funciona con nodos no organizativos)
- Se puede eliminar un nodo del árbol y añadirlo a la lista de elementos excluidos pulsando la tecla 'SUPR'
### Versión 1.4.0
- Se ha añadido la posibilidad de cambiar la fuente de los iconos de GlyphIcons a FontAwesome
### Versión 1.3.0
-	Se ha mejorado la visualización del árbol, añadiendo el icono de carpeta cerrada
-	Se ha añadido un menú que permite visualizar el menú "Acerca de"
### Versión 1.2.0
-	Se ha añadido la visualización gráfica del arbol que se genera a su derecha
-	Se ha mejorado el sistema exclusión de carpetas y ficheros desde el listado de exclusiones
### Versión 1.1.0
-   Se puede añadir una lista de exclusiones. Se pueden excluir nombres de carpeta, nombres de fichero y extensiones de archivo
-   Si dentro de la carpeta principal se encuentra un fichero  **.gitignore**, lo leerá y añadirá a lista de exclusiones los mismos elementos que se ignoran en **.gitignore**
-   Por defecto, añadirá a la lista de exclusiones la carpeta  **.git**
### Versión 1.0.0
-   Seleccionar una carpeta y ver su representación en un árbol, apta para la visualización de esa estructura en formato de carpetas y ficheros
-   Volver a construir el árbol sin seleccionar la carpeta nuevamente
-   Copiar el contenido en el portapapeles mediante un botón
