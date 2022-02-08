function toggleItem(id) {
    const item = document.getElementById(id);
    if (item) {
        const hidden = item.style.display === "none";
        item.style.display = hidden ? "block" : "none";
    }
}