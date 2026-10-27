import { mountReactMfe } from './mountReactMfe.js';
import { Bikes } from '../components/Bikes.jsx';

mountReactMfe('mfe-bikes-react', '#bikes-root', ({ triggerMfeEvent }) => {
  const onAdd = (bike) =>
    triggerMfeEvent(`add to cart bike with ${bike.id}`, { type: 'add', id: String(bike.id) });
  return <Bikes onAdd={onAdd} />;
});
