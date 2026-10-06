import { useState } from 'react';
import { calculate } from '../api/beamApi';

const sections = [
  { value: 'CYLINDER', label: 'Solid Cylinder' },
  { value: 'DONUT', label: 'Donut (Hollow Cylinder)' },
  { value: 'SQUARE', label: 'Solid Square' },
  { value: 'HOLLOW_SQUARE', label: 'Hollow Square' },
  { value: 'I_SHAPED', label: 'I-Beam' },
  { value: 'T_SHAPED', label: 'T-Beam' }
];

const loads = [
  { value: 'AXIAL', label: 'Traction/Compression' },
  { value: 'TORSION', label: 'Twist (Torsion)' },
  { value: 'SHEAR', label: 'Shear' }
];

export default function InputForm({ onResult }) {
  const [form, setForm] = useState({
    crossSection: 'CYLINDER',
    loadType: 'AXIAL',
    length: 1.0,
    outerDiameter: 0.1,
    innerDiameter: 0.05,
    height: 0.2,
    width: 0.1,
    flangeThickness: 0.01,
    webThickness: 0.008,
    outerSize: 0.1,
    wallThickness: 0.01,
    sideLength: 0.1,
    axialForce: 0,
    torque: 0,
    shearForce: 0,
    youngsModulus: 200e9,
    shearModulus: 79.4e9
  });
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : type === 'number' ? parseFloat(value) : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await calculate(form);
      onResult(res);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const fmt = (v, unit) => v !== null && v !== undefined ? v.toExponential(3) + ' ' + unit : '-';

  return (
    <form onSubmit={handleSubmit}>
      <h2>Beam Properties</h2>
      <label>
        Cross-section:
        <select name="crossSection" value={form.crossSection} onChange={handleChange}>
          {sections.map(s => (
            <option key={s.value} value={s.value}>{s.label}</option>
          ))}
        </select>
      </label>

      {form.crossSection === 'CYLINDER' && (
        <>
          <label>Outer Diameter (m): <input name="outerDiameter" type="number" step="0.000001" min="0" value={form.outerDiameter} onChange={handleChange} /></label>
          <label>Inner Diameter (m): <input name="innerDiameter" type="number" step="0.000001" min="0" value={form.innerDiameter} onChange={handleChange} /></label>
        </>
      )}

      {form.crossSection === 'SQUARE' && (
        <label>Side Length (m): <input name="sideLength" type="number" step="0.000001" min="0" value={form.sideLength} onChange={handleChange} /></label>
      )}

      {form.crossSection === 'HOLLOW_SQUARE' && (
        <>
          <label>Outer Size (m): <input name="outerSize" type="number" step="0.000001" min="0" value={form.outerSize} onChange={handleChange} /></label>
          <label>Wall Thickness (m): <input name="wallThickness" type="number" step="0.000001" min="0" value={form.wallThickness} onChange={handleChange} /></label>
        </>
      )}

      {(form.crossSection === 'I_SHAPED' || form.crossSection === 'T_SHAPED') && (
        <>
          <label>Height (m): <input name="height" type="number" step="0.000001" min="0" value={form.height} onChange={handleChange} /></label>
          <label>Width (m): <input name="width" type="number" step="0.000001" min="0" value={form.width} onChange={handleChange} /></label>
          <label>Flange Thickness (m): <input name="flangeThickness" type="number" step="0.000001" min="0" value={form.flangeThickness} onChange={handleChange} /></label>
          <label>Web Thickness (m): <input name="webThickness" type="number" step="0.000001" min="0" value={form.webThickness} onChange={handleChange} /></label>
        </>
      )}

      <label>Beam Length (m): <input name="length" type="number" step="0.000001" min="0" value={form.length} onChange={handleChange} /></label>

      <h2>Load</h2>
      <label>
        Load type:
        <select name="loadType" value={form.loadType} onChange={handleChange}>
          {loads.map(l => (
            <option key={l.value} value={l.value}>{l.label}</option>
          ))}
        </select>
      </label>

      {form.loadType === 'AXIAL' && (
        <label>Axial Force F (N): <input name="axialForce" type="number" step="0.000001" min="0" value={form.axialForce} onChange={handleChange} /></label>
      )}

      {form.loadType === 'TORSION' && (
        <label>Torque T (N·m): <input name="torque" type="number" step="0.000001" min="0" value={form.torque} onChange={handleChange} /></label>
      )}

      {form.loadType === 'SHEAR' && (
        <label>Shear Force V (N): <input name="shearForce" type="number" step="0.000001" min="0" value={form.shearForce} onChange={handleChange} /></label>
      )}

      <details style={{ marginTop: '1rem' }}>
        <summary>Advanced: Material Properties (optional)</summary>
        <p style={{ fontSize: '0.9rem', color: '#666' }}>Defaults: Steel (E=200 GPa, G=79.4 GPa)</p>
        <label>Youngs Modulus E (Pa): <input name="youngsModulus" type="number" step="1e6" min="0" value={form.youngsModulus} onChange={handleChange} /></label>
        <label>Shear Modulus G (Pa): <input name="shearModulus" type="number" step="1e6" min="0" value={form.shearModulus} onChange={handleChange} /></label>
      </details>

      <button type="submit" disabled={loading}>
        {loading ? 'Calculating...' : 'Calculate Stress'}
      </button>

      {error && <p style={{ color: '#d32f2f', marginTop: '1rem' }}>{error}</p>}
    </form>
  );
}