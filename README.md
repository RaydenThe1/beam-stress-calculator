# Beam Stress Calculator

Full-stack web app to compute axial, torsional, and shear stresses in beams of various cross-sections, plus displaced shape visualization.

## Tech Stack
- **Backend**: Java 17, Spring Boot 3
- **Frontend**: React 18 + Vite
- **Containerization**: Docker & Docker Compose (Kubernetes manifests included)

## Quick Start (Docker Compose)
```bash
docker compose up --build
```
Then open <http://localhost:8081> in your browser.

## API
`POST /api/v1/calculate` consumes/produces JSON. See `backend/src/main/java/com/beamcalc/api/dto/CalculationRequest.java` for fields.

## Test Suite
```bash
cd backend
./mvnw test
```

## Notes
- All units are SI (meters, Newtons, Pascals).
- Default material: structural steel (E=200 GPa, G=79.4 GPa).
- Visualizations exaggerate displacement for clarity.
