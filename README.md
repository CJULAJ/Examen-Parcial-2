# Agenda de Citas - Variante C

Este proyecto es una aplicación sencilla para administrar las citas de un negocio de servicios.

Permite registrar las citas de los clientes, ver las citas guardadas, modificar su información y eliminarlas cuando sea necesario.

El proyecto fue desarrollado utilizando **Java, Swing, Maven, JDBC y MySQL**.

## ¿Qué puede hacer el programa?

El sistema permite realizar las cuatro operaciones principales de un CRUD:

- Crear una nueva cita.
- Ver todas las citas registradas.
- Actualizar una cita existente.
- Eliminar una cita.

Cada cita guarda la siguiente información:

- Nombre del cliente.
- Fecha de la cita.
- Hora de la cita.
- Servicio solicitado.
- Duración aproximada en minutos.
- Estado de la cita.

Los estados disponibles son:

- Pendiente
- Confirmada
- Cancelada

El estado se selecciona desde un `JComboBox`, por lo que no se puede ingresar un estado diferente a los permitidos.

## Interfaz gráfica

La aplicación utiliza **Java Swing** para mostrar la interfaz gráfica.

La ventana principal tiene un formulario para ingresar los datos de una cita y una tabla donde se muestran todas las citas guardadas.

También tiene botones para:

- Guardar
- Actualizar
- Eliminar
- Limpiar

Para actualizar o eliminar una cita primero se selecciona desde la tabla.

Antes de eliminar una cita, el programa muestra un mensaje de confirmación.

## Validaciones

Antes de guardar la información se realizan algunas validaciones.

El programa verifica que:

- Los campos obligatorios no estén vacíos.
- La duración sea mayor a 0 minutos.
- La fecha y hora de una cita nueva no hayan pasado.
- El estado sea uno de los valores permitidos.

Si ocurre un problema, se muestra un mensaje en pantalla en lugar de cerrar el programa.

## Base de datos

El proyecto utiliza **MySQL** para guardar la información.

Se utiliza una sola tabla llamada:

`citas`

La tabla almacena:

- `id`
- `cliente`
- `fecha_hora`
- `servicio`
- `duracion_minutos`
- `estado`

El `id` se genera automáticamente.

La fecha y hora se guardan juntas utilizando `DATETIME`.

El estado solamente permite:

`pendiente`, `confirmada` o `cancelada`.

El archivo para crear la base de datos y la tabla se encuentra en:

`citas-ui/sql/schema.sql`

## Estructura del proyecto

El proyecto utiliza Maven multi-módulo y está dividido principalmente en dos módulos:

### citas-core

Contiene la lógica relacionada con los datos.

Aquí se encuentran:

- El modelo `Cita`.
- La conexión con MySQL.
- El DAO encargado de las operaciones CRUD.
- El uso de JDBC y `PreparedStatement`.

Este módulo no depende de Swing.

### citas-ui

Contiene la interfaz gráfica del programa.

Aquí se encuentran:

- `MainUI`
- `VentanaPrincipal`
- El formulario.
- La tabla de citas.
- Los botones y mensajes mostrados al usuario.

`citas-ui` utiliza `citas-core` como una dependencia de Maven.

De esta forma, la interfaz no necesita trabajar directamente con JDBC.

## Funcionamiento general

El funcionamiento del programa es básicamente:

`Usuario → Interfaz Swing → CitaDAO → MySQL`

Por ejemplo, cuando se guarda una cita:

1. El usuario llena el formulario.
2. El programa valida los datos.
3. Se crea un objeto `Cita`.
4. La interfaz llama al método `crear()` de `CitaDAO`.
5. El DAO utiliza JDBC para guardar la cita en MySQL.
6. La tabla de la interfaz se actualiza para mostrar la nueva cita.

El mismo patrón se utiliza para actualizar y eliminar.

## DAO

`CitaDAO` contiene las operaciones principales para trabajar con la base de datos:

- `crear()`
- `listarTodos()`
- `buscarPorId()`
- `actualizar()`
- `eliminar()`

Las consultas SQL utilizan `PreparedStatement`.

## Cómo ejecutar el proyecto

Primero se debe tener MySQL funcionando y ejecutar el archivo:

`citas-ui/sql/schema.sql`

Después se deben configurar el usuario y contraseña de MySQL en la clase de conexión del módulo `citas-core`.

Para comprobar que los módulos Maven funcionan correctamente se puede ejecutar desde la carpeta principal:

`mvn clean install`

Finalmente, se ejecuta la clase:

`MainUI.java`

como una aplicación Java.

Al iniciar aparecerá la ventana de la Agenda de Citas.