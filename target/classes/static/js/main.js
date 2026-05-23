// FreshMart – main.js

// Auto-dismiss alerts after 4 seconds
document.addEventListener('DOMContentLoaded', () => {
    const alerts = document.querySelectorAll('.auto-dismiss');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.5s';
            setTimeout(() => alert.remove(), 500);
        }, 4000);
    });
});

// Confirm delete/cancel dialogs
function confirmDelete(message) {
    return confirm(message || 'Are you sure you want to delete this item?');
}

// Cart quantity +/- buttons
function changeQty(input, delta) {
    const current = parseInt(input.value) || 1;
    const newVal = Math.max(0, current + delta);
    input.value = newVal;
    // Auto submit the form when qty changes
    const form = input.closest('form');
    if (form) form.submit();
}

// Search form: submit on Enter
document.addEventListener('DOMContentLoaded', () => {
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keydown', e => {
            if (e.key === 'Enter') {
                e.target.closest('form').submit();
            }
        });
    }
});

