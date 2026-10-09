/**
 * MubeenMart Global Frontend Utilities
 */
document.addEventListener('DOMContentLoaded', () => {
    // Quick Add to Cart with AJAX
    const ajaxCartButtons = document.querySelectorAll('.ajax-add-cart');
    ajaxCartButtons.forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.preventDefault();
            const productId = btn.getAttribute('data-product-id');
            const originalText = btn.textContent;
            btn.textContent = 'Adding...';
            btn.disabled = true;

            try {
                const contextPath = window.CONTEXT_PATH || '';
                const response = await fetch(`${contextPath}/cart`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'Accept': 'application/json'
                    },
                    body: `action=add&productId=${productId}&quantity=1`
                });

                if (response.status === 401) {
                    window.location.href = `${contextPath}/login?redirect=${encodeURIComponent(window.location.pathname)}`;
                    return;
                }

                if (response.redirected) {
                    window.location.href = response.url;
                    return;
                }

                const data = await response.json();
                if (data.success) {
                    btn.textContent = '✓ Added!';
                    btn.classList.remove('btn-primary');
                    btn.classList.add('btn-secondary');

                    // Update cart count badge in navbar
                    const cartCountBadge = document.getElementById('navbarCartCount');
                    if (cartCountBadge && data.data && data.data.cartCount !== undefined) {
                        cartCountBadge.textContent = data.data.cartCount;
                    }

                    setTimeout(() => {
                        btn.textContent = originalText;
                        btn.classList.remove('btn-secondary');
                        btn.classList.add('btn-primary');
                        btn.disabled = false;
                    }, 1500);
                } else {
                    alert(data.error ? data.error.message : 'Failed to add item to cart');
                    btn.textContent = originalText;
                    btn.disabled = false;
                }
            } catch (err) {
                console.error('Add to cart failed:', err);
                btn.textContent = originalText;
                btn.disabled = false;
            }
        });
    });

    // Quick Add to Wishlist with AJAX
    const ajaxWishlistButtons = document.querySelectorAll('.ajax-add-wishlist');
    ajaxWishlistButtons.forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.preventDefault();
            const productId = btn.getAttribute('data-product-id');
            const originalText = btn.textContent;
            btn.textContent = '...';
            btn.disabled = true;

            try {
                const contextPath = window.CONTEXT_PATH || '';
                const response = await fetch(`${contextPath}/wishlist/add`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                        'Accept': 'application/json'
                    },
                    body: `productId=${productId}`
                });

                if (response.status === 401) {
                    window.location.href = `${contextPath}/login?redirect=${encodeURIComponent(window.location.pathname)}`;
                    return;
                }

                if (response.redirected) {
                    window.location.href = response.url;
                    return;
                }

                const data = await response.json();
                if (data.success) {
                    btn.textContent = '❤️';
                    btn.title = 'Saved in Wishlist';
                    btn.disabled = false;
                } else {
                    alert(data.error ? data.error.message : 'Failed to save to wishlist');
                    btn.textContent = originalText;
                    btn.disabled = false;
                }
            } catch (err) {
                console.error('Wishlist action failed:', err);
                btn.textContent = originalText;
                btn.disabled = false;
            }
        });
    });
});
