export const calculate = async (request) => {
  const res = await fetch('/api/v1/calculate', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(request)
  });
  if (!res.ok) {
    const err = await res.json();
    throw new Error(err.error || 'Calculation failed');
  }
  return res.json();
};
