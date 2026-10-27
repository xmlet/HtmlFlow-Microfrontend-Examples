console.log("Loading script of mfe2!")


mfe('mfe2', ({ root, listenMfeEvent, reloadMfe }) => {
    if (root && !root.cartClickHandlerBound) {
        root.cartClickHandlerBound = true;
        root.addEventListener('click', (e) => {
            const button = e.target.closest('button');
            const id = button?.getAttribute('value');
            switch (button?.id) {
                case 'triggerOrder':
                    window.location.href = '/checkout';
                    break;
                case 'increaseQuantity':
                    handleQuantity('increase', id).then(() => reloadMfe());
                    break;
                case 'decreaseQuantity':
                    handleQuantity('decrease', id).then(() => reloadMfe());
                    break;
                case 'deleteCartItem':
                    deleteItem(id).then(() => reloadMfe());
                    break;
            }
        });
    }

    listenMfeEvent((e) => {
        if(e.detail?.payload?.type === 'add'){
            addBikeToCart(e.detail?.payload?.id).then(() => reloadMfe());
        }
    });

});

async function makeApiCall({ endpoint, method = 'GET' }) {
    const url = `http://localhost:8082${endpoint}`;
    try {
        const response = await fetch(url, { method, headers: {} });
        if (!response.ok) {
            console.error('API call failed with status:', response.status);
        }
        return response;
    } catch (error) {
        console.error(`Error during ${method} to ${url}:`, error);
        throw error;
    }
}

async function finalizeOrder() {
    await makeApiCall({ endpoint: '/order/finalise', method: 'POST' });
}

async function handleQuantity(type, id) {
    await makeApiCall({ endpoint: `/cart/updateQuantity/${id}/${type}`, method: 'POST' });
}

async function deleteItem(id) {
    await makeApiCall({ endpoint: `/cart/delete/${id}`, method: 'POST' });
}

async function addBikeToCart(id) {
    await makeApiCall({ endpoint: `/cart/add/${id}`, method: 'POST' });
}