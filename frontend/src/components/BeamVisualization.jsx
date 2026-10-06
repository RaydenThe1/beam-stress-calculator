import { useEffect, useRef, useState } from 'react';

export default function BeamVisualization({ result }) {
  const svgRef = useRef(null);
  const [dimensions, setDimensions] = useState({ width: 300, height: 200 });

  useEffect(() => {
    const updateSize = () => {
      if (svgRef.current) {
        const rect = svgRef.current.getBoundingClientRect();
        setDimensions({ width: rect.width, height: rect.height });
      }
    };
    updateSize();
    window.addEventListener('resize', updateSize);
    return () => window.removeEventListener('resize', updateSize);
  }, []);

  if (!result) return null;

  const { width, height } = dimensions;
  const cx = width / 2;
  const cy = height / 2;
  const scale = Math.min(width, height) * 0.4;
  const exaggeration = 50; // amplify displacement for visibility

  // Helper to map stress to color intensity (0-1)
  const stressIntensity = (stress) => {
    if (stress == null) return 0;
    const maxStress = 250e6; // 250 MPa reference
    return Math.min(1, Math.abs(stress) / maxStress);
  };

  let paths = '';
  let stressOverlay = '';

  switch (result.formulaUsed?.includes('F/A') ? 'axial' :
          result.formulaUsed?.includes('T*r/J') ? 'torsion' : 'shear') {
    case 'axial': {
      const r = result.sectionProperties.maxRadius;
      const disp = result.displacementTractionCompression || 0;
      const bend = disp * exaggeration;
      // Top fiber in tension/compression, bottom opposite
      const top = stressIntensity(result.stressTractionCompression);
      const bot = top; // same magnitude
      paths += `
        <path d="M${cx - r*scale} ${cy}
                 L${cx - r*scale} ${cy - bend}
                 L${cx + r*scale} ${cy + bend}
                 L${cx + r*scale} ${cy}
                 Z" fill="none" stroke="#333" stroke-width="2"/>
      `;
      stressOverlay += `
        <rect x="${cx - r*scale}" y="${cy - r*scale - bend}" width="${2*r*scale}" height="${2*r*scale}"
              fill="url(#grad-top)" opacity="${top * 0.3}"/>
        <rect x="${cx - r*scale}" y="${cy + bend}" width="${2*r*scale}" height="${2*r*scale}"
              fill="url(#grad-bottom)" opacity="${bot * 0.3}"/>
      `;
      break;
    }
    case 'torsion': {
      const r = result.sectionProperties.maxRadius;
      const tau = result.stressTorsion || 0;
      const intensity = stressIntensity(tau);
      // Draw twisted square as approximation
      const size = r * 2 * scale;
      paths += `
        <rect x="${cx - size/2}" y="${cy - size/2}" width="${size}" height="${size}"
              fill="none" stroke="#333" stroke-width="2" transform="rotate(${result.displacementTorsion * exaggeration * 5}, ${cx}, ${cy})"/>
      `;
      stressOverlay += `
        <circle cx="${cx}" cy="${cy}" r="${r*scale}" fill="none" stroke="url(#grad-torsion)" stroke-width="${4}" opacity="${intensity * 0.5}"/>
      `;
      break;
    }
    case 'shear': {
      const h = result.sectionProperties.maxRadius * 2 * scale;
      const w = h * 0.6; // approximate I-beam web width
      const tau = result.stressShear || 0;
      const intensity = stressIntensity(tau);
      paths += `
        <rect x="${cx - w/2}" y="${cy - h/2}" width="${w}" height="${h}"
              fill="none" stroke="#333" stroke-width="2"/>
      `;
      stressOverlay += `
        <rect x="${cx - w/2}" y="${cy - h/2}" width="${w}" height="${h}"
              fill="url(#grad-shear)" opacity="${intensity * 0.4}"/>
      `;
      break;
    }
    default:
      // fallback: just draw outline
      const r = result.sectionProperties.maxRadius || 0.05;
      paths += `
        <circle cx="${cx}" cy="${cy}" r="${r*scale}" fill="none" stroke="#999" stroke-width="1" opacity="0.5"/>
      `;
  }

  return (
    <div className="card">
      <h2>Beam Visualization</h2>
      <svg ref={svgRef} width={width} height={height}>
        <defs>
          <linearGradient id="grad-top" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stopColor="#d32f2f" />
            <stop offset="100%" stopColor="#ffebee" />
          </linearGradient>
          <linearGradient id="grad-bottom" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stopColor="#1976d2" />
            <stop offset="100%" stopColor="#e3f2fd" />
          </linearGradient>
          <linearGradient id="grad-torsion" x1="0%" y1="0%" x2="100%" y2="0%">
            <stop offset="0%" stopColor="#f57c00" />
            <stop offset="100%" stopColor="#fff8e1" />
          </linearGradient>
          <linearGradient id="grad-shear" x1="0%" y1="0%" x2="0%" y2="100%">
            <stop offset="0%" stopColor="#388e3c" />
            <stop offset="100%" stopColor="#e8f5e9" />
          </linearGradient>
        </defs>
        {/* Neutral outline */}
        <circle cx={cx} cy={cy} r={result.sectionProperties.maxRadius * scale} fill="none" stroke="#999" stroke-width="1" opacity="0.5"/>
        {/* Stressed shape */}
        {paths}
        {/* Stress intensity overlay */}
        {stressOverlay}
      </svg>
    </div>
  );
}
