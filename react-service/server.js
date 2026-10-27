import express from 'express';
import cors from 'cors';

const PORT = Number(process.env.PORT ?? 8084);

const app = express();

app.use(cors());
app.use(express.static('dist/client'));

app.get('/bikes/view', (req, res) => {
  res.type('html').send('<div id="bikes-root"><p>Loading bikes…</p></div>');
});

app.get('/health', (req, res) => res.send('ok'));

app.listen(PORT, () => console.log(`react-service listening on http://localhost:${PORT}`));
