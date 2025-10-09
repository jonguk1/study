document.addEventListener('DOMContentLoaded', function() {
    if (document.getElementById('add-btn')) {
        document.getElementById('add-btn').addEventListener('click', addTodo);
    }
    if (document.getElementById('todo-input')) {
        document.getElementById('todo-input').addEventListener('keydown', function(e) {
            if (e.key === 'Enter') addTodo();
        });
    }
    if (typeof loadTodos === 'function') loadTodos();
});

function loadTodos() {
    fetchWithAuth('/api/todos')
        .then(res => res.json())
        .then(data => {
            const list = document.getElementById('todo-list');
            list.innerHTML = '';
            data.forEach(todo => list.appendChild(createTodoItem(todo)));
        });
}

function addTodo() {
    const input = document.getElementById('todo-input');
    const content = input.value.trim();
    if (!content) {
        showError('할 일을 입력하세요.');
        return;
    }
    fetchWithAuth('/api/todos', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({content, completed: false})
    })
    .then(res => res.ok ? res.json() : res.json().then(err => { throw new Error(err.message); }))
    .then(() => {
        input.value = '';
        loadTodos();
    })
    .catch(err => showError(err.message));
}

function createTodoItem(todo) {
    const li = document.createElement('li');
    li.className = 'list-group-item d-flex justify-content-between align-items-center';
    if (todo.completed) li.classList.add('completed');

    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.className = 'form-check-input me-2';
    checkbox.checked = todo.completed;
    checkbox.style.borderRadius = '50%';
    checkbox.onclick = () => {
        fetchWithAuth(`/api/todos/${todo.id}/toggle`, {method: 'PUT'})
            .then(loadTodos);
    };

    const span = document.createElement('span');
    span.textContent = todo.content;
    span.style.flex = '1';
    if (todo.completed) span.style.textDecoration = 'line-through';

    span.ondblclick = () => {
        const input = document.createElement('input');
        input.type = 'text';
        input.value = todo.content;
        input.className = 'form-control form-control-sm';
        input.style.flex = '1';
        input.onblur = saveEdit;
        input.onkeydown = function(e) {
            if (e.key === 'Enter') input.blur();
        };
        li.replaceChild(input, span);
        input.focus();

        function saveEdit() {
            const newContent = input.value.trim();
            if (newContent && newContent !== todo.content) {
                fetchWithAuth(`/api/todos/${todo.id}`, {
                    method: 'PUT',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({content: newContent, completed: todo.completed})
                })
                .then(res => res.ok ? res.json() : res.json().then(err => { throw new Error(err.message); }))
                .then(loadTodos)
                .catch(err => showError(err.message));
            } else {
                li.replaceChild(span, input);
            }
        }
    };

    const delBtn = document.createElement('button');
    delBtn.className = 'btn btn-sm btn-outline-danger ms-2';
    delBtn.textContent = '🗑️';
    delBtn.title = '삭제';
    delBtn.onclick = () => {
        if (confirm('삭제하시겠습니까?')) {
            fetchWithAuth(`/api/todos/${todo.id}`, {method: 'DELETE'})
                .then(loadTodos)
                .catch(err => showError(err.message));
        }
    };

    li.append(checkbox, span, delBtn);
    return li;
}

// 인증이 필요한 API 요청은 반드시 fetchWithAuth 사용
function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem('jwtToken');
    return fetch(url, {
        ...options,
        headers: {
            ...options.headers,
            'Authorization': token ? 'Bearer ' + token : ''
        }
    });
}

function showError(message) {
    const errorDiv = document.getElementById('error-message');
    errorDiv.textContent = message;
    errorDiv.classList.remove('d-none');
    clearTimeout(showError.timeoutId);
    showError.timeoutId = setTimeout(() => {
        errorDiv.classList.add('d-none');
        errorDiv.textContent = '';
    }, 2000);
}
