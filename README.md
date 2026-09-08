# TA_EX - Automatizacion de Pruebas

Proyecto academico Java 17/Maven que demuestra control de versiones, pruebas unitarias y de integracion, CI con GitHub Actions, acceptance tests, despliegue de prueba Blue-Green y rollback automatico.

## Estrategia de ramas
Se utiliza Trunk-Based Development: `main` es la linea estable y el trabajo se realiza en ramas cortas `feature/*` o `fix/*`. Cada cambio entra mediante pull request y debe superar CI.

## Estructura
- `src/main/java`: aplicacion HTTP minima.
- `src/test/java`: pruebas JUnit 5; `*Test` unitarias y `*IT` integracion.
- `.github/workflows/ci.yml`: build y pruebas.
- `.github/workflows/deploy-test.yml`: deployment pipeline Blue-Green.
- `scripts/`: acceptance test, despliegue local y rollback.
- `evidence/`: capturas reales incorporadas por el estudiante.

## Requisitos
Java 17, Maven 3.9 o compatible, Git, Bash y curl.

## Ejecucion local
```bash
mvn clean test       # unitarias
mvn clean verify     # unitarias + integracion + JAR
java -jar target/automatizacion-pruebas-ta.jar
./scripts/acceptance-test.sh http://localhost:8080
```

## Despliegue local Blue-Green
```bash
mvn clean verify
./scripts/deploy-blue-green.sh
cat .runtime/active-slot .runtime/active-port
./scripts/deploy-blue-green.sh
./scripts/rollback.sh
./scripts/cleanup.sh
```
El candidato solo se activa despues del acceptance gate. Si falla, el slot estable permanece activo.

## CI/CD
`ci.yml` se ejecuta en push y pull request. `deploy-test.yml` se ejecuta manualmente desde Actions. Con `simulate_failure=false` demuestra el switch a Green; con `true` provoca un fallo controlado y demuestra rollback a Blue.

## Evidencias requeridas
1. Grafico de ramas o pull request aprobado.
2. Ejecucion local de `mvn clean verify` con BUILD SUCCESS.
3. Workflow CI verde y artefacto descargable.
4. Deploy con `SWITCH_OK` y `FINAL_STATE`.
5. Deploy con fallo controlado y `ROLLBACK_OK`.

## Seguridad y configuracion
No se versionan secretos. Las configuraciones variables se entregan por variables de entorno. Los artefactos y reportes quedan vinculados al numero de ejecucion de GitHub Actions.
