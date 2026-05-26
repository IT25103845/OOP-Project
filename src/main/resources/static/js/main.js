// FreshMart Component 1 – User Authentication JS

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

// Confirm user delete dialog
function confirmDelete(message) {
    return confirm(message || 'Are you sure you want to delete this user?');
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
