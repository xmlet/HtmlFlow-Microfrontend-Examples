import { createRoot } from 'react-dom/client';

/**
 * Mounts a client-side rendered React tree inside the shadow root created by the HtmlFlow MFE engine.
 *
 * The MFE is configured without a listen event, so the engine loads the fragment (an empty mount point)
 * once and never reloads it: React owns the content from then on.
 *
 */
export function mountReactMfe(name, selector, render) {
  window.mfe(name, (ctx) => {
    const container = ctx.root.querySelector(selector);
    if (!container) {
      console.error(`MFE -> ${name}: mount point "${selector}" not found in the fragment.`);
      return;
    }
    createRoot(container).render(render(ctx));
  });
}
