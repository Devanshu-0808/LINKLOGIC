document.addEventListener('DOMContentLoaded', (event) => {
    const switchInput = document.getElementById('Switch');
    const formWrap = document.querySelector('.form_wrap');
    const inputs = document.querySelectorAll('.form_wrap input[type="text"]');
    const createNewBtn = document.querySelector('.CreateNewBtn');
    const createNewInfo = document.querySelector('.CreateNewinfo');
    const h4 = document.querySelector('.form_wrap h4');
    const inp = document.getElementById("in1");
    const inp2 = document.getElementById("in2");
    const in3=document.getElementById("inputID");
    const form = document.querySelector('.form_wrap form');
  
    if (!switchInput || !formWrap || !createNewBtn || !createNewInfo || !h4 || !inp) {
        return;
    }

    // Set initial mode based on the switch state
    if (switchInput.checked) {
        setLightMode();
    } else {
        setDarkMode();
    }

    // Toggle mode on switch change
    switchInput.addEventListener('change', () => {
        if (switchInput.checked) {
            setLightMode();
        } else {
            setDarkMode();
        }
    });

    function setLightMode() {
        formWrap.classList.remove('dark-mode');
        formWrap.classList.add('light-mode');
        inputs.forEach(input => {
            input.classList.remove('dark-mode');
            input.classList.add('light-mode');
        });
        createNewBtn.classList.remove('dark-mode');
        createNewBtn.classList.add('light-mode');
        createNewInfo.classList.remove('dark-mode');
        createNewInfo.classList.add('light-mode');
        h4.classList.remove('dark-mode');
        h4.classList.add('light-mode');
    }

    function setDarkMode() {
        formWrap.classList.remove('light-mode');
        formWrap.classList.add('dark-mode');
        inputs.forEach(input => {
            input.classList.remove('light-mode');
            input.classList.add('dark-mode');
        });
        createNewInfo.classList.remove('light-mode');
        createNewInfo.classList.add('dark-mode');
        h4.classList.remove('light-mode');
        h4.classList.add('dark-mode');
    }

    createNewInfo.addEventListener('click', (event) => {
        event.preventDefault();
        fetch('/session/groupId')
            .then(response => response.json())
            .then(data => {
                inp.value = data.groupId;
                showAlert('Group ID Created: ' + data.groupId); // Call showAlert here
            })
            .catch(error => console.error('Error fetching group ID:', error));
    });


    
function showAlert(message) {
    const alertDiv = document.createElement('div');
    alertDiv.className = 'custom-alert';
    alertDiv.textContent = message;
    document.body.appendChild(alertDiv);

    setTimeout(() => {
        alertDiv.style.opacity = '0';
        setTimeout(() => {
            document.body.removeChild(alertDiv);
        }, 300);
    }, 1000);
}

   
});