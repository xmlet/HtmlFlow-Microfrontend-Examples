import { useEffect, useMemo, useState } from 'react';

const BIKE_SERVICE_URL = import.meta.env.VITE_BIKE_SERVICE_URL ?? 'http://localhost:8081';

/**
 * Client-side rendered bike catalog. The data comes from the JSON API of the Java bike-service
 * (`GET /bikes`); all markup is produced by React in the browser.
 */
export function Bikes({ onAdd }) {
  const [bikes, setBikes] = useState(null);
  const [error, setError] = useState(null);
  const [query, setQuery] = useState('');

  useEffect(() => {
    const controller = new AbortController();
    fetch(`${BIKE_SERVICE_URL}/bikes`, { signal: controller.signal })
      .then((response) => {
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        return response.json();
      })
      .then(setBikes)
      .catch((e) => {
        if (e.name !== 'AbortError') setError(e.message);
      });
    return () => controller.abort();
  }, []);

  const visibleBikes = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!bikes || !q) return bikes ?? [];
    return bikes.filter((bike) => `${bike.model} ${bike.details}`.toLowerCase().includes(q));
  }, [bikes, query]);

  if (error) return <p className="bikes__error">{`Could not load the bikes: ${error}`}</p>;
  if (!bikes) return <p>Loading bikes…</p>;

  return (
    <main className="bikes">
      <div className="bikes__search">
        <label htmlFor="search">Search</label>
        <input
          id="search"
          type="text"
          placeholder="Search bikes..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </div>

      <div className="bikes__list">
        {visibleBikes.length === 0 && <p>No bikes match your search.</p>}
        {visibleBikes.map((bike) => (
          <div key={bike.id} className="bike__box">
            <h4>{`Model: ${bike.model}`}</h4>
            <img src={bike.imageSource} height="70" width="70" alt={bike.model} />
            <span>{`Stock: ${bike.availableStock}`}</span>
            <span>{`Details: ${bike.details}`}</span>
            <span>{`Electric: ${bike.electric ? 'Yes' : 'No'}`}</span>
            <span>{`Price: ${bike.price}€`}</span>
            <button type="button" className="btn" disabled={!bike.inStock} onClick={() => onAdd(bike)}>
              Add to cart
            </button>
          </div>
        ))}
      </div>

      <p className="bikes__footer">Client-side rendered by React</p>
    </main>
  );
}
