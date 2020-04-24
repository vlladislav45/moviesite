// Listen for form submit
document.addEventListener('submit', e => {
    e.preventDefault();

    const formData = e.target;

    fetch(formData.action, {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        body: new FormData(formData),
    }).then(response => {
        console.log(response)
    })
})