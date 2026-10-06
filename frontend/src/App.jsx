import { useState } from 'react';
import InputForm from './components/InputForm';
import BeamVisualization from './components/BeamVisualization';
import './styles.css';

export default function App() {
  const [result, setResult] = useState(null);
  return (
    <div className="container">
      <h1>Beam Stress Calculator</h1>
      <div className="card">
        <InputForm onResult={setResult} />
      </div>
      <div className="card">
        <BeamVisualization result={result} />
      </div>
    </div>
  );
}