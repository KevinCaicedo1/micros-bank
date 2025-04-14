# Microservicios Spring Boot: Customer y Movements

Este repositorio contiene dos microservicios Spring Boot, "customer" y "movements", diseñados para operar de forma independiente y comunicarse entre sí.

## Requisitos Previos

* **Docker:** Asegúrate de tener Docker instalado en tu sistema. Puedes descargarlo desde [https://www.docker.com/get-started](https://www.docker.com/get-started).
* **Docker Compose:** Docker Compose también es necesario. Generalmente se instala junto con Docker Desktop.

## Despliegue Rápido con Docker Compose

La forma más sencilla de poner en marcha ambos microservicios es utilizando Docker Compose. Sigue estos pasos:

1.  **Clona el repositorio:**

    ```bash
    git clone https://github.com/KevinCaicedo1/micros-bank
    cd micros-bank
    ```

2.  **Ejecuta Docker Compose:**

    ```bash
    docker-compose up --build
    ```

    Este comando construirá las imágenes Docker necesarias y levantará los contenedores de ambos microservicios. La opción `--build` asegura que las imágenes se construyan si hay cambios en los Dockerfiles.

## Acceso a los Microservicios

Una vez que los contenedores estén en ejecución, podrás acceder a los microservicios a través de los siguientes puertos:

* **Customer:** `http://localhost:[puerto del servicio customer]`
* **Movements:** `http://localhost:[puerto del servicio movements]`

    * Nota: Deberas revisar el archivo docker-compose.yml para revisar los puertos asignados a los microservicios.

## Consideraciones Adicionales

* Este proyecto utiliza Gradle para la gestión de dependencias y la construcción.
* Los archivos `application.properties` o `application.yml` se encuentran dentro de cada directorio de microservicio (`customer` y `movements`). Puedes modificar estos archivos para ajustar la configuración de cada servicio.
* En caso de que se realicen cambios en los microservicios, sera necesario volver a ejecutar el comando `docker-compose up --build` para que docker genere nuevamente las imagenes y los contenedores con los ultimos cambios realizados.
* En caso de querer detener la ejecucion de los microservicios, se debe de ejecutar el comando `docker-compose down`.

## Contribución

¡Las contribuciones son bienvenidas! Si encuentras algún problema o tienes sugerencias de mejora, no dudes en abrir un issue o enviar un pull request.

## Licencia

[Indica la licencia bajo la que se distribuye el proyecto]
