// 1. 페이지가 로드되면 할 일 목록을 불러오고, 추가 버튼에 이벤트를 등록
document.addEventListener('DOMContentLoaded', loadTodos);
document.getElementById('add-btn').addEventListener('click', addTodo);
// 아래 한 줄 추가: 입력창에서 엔터키로도 추가
document.getElementById('todo-input').addEventListener('keydown', function(e) {
    if (e.key === 'Enter') addTodo();
});

// 2. 할 일 전체 목록을 서버에서 받아와 화면에 표시
function loadTodos() {
    fetch('/api/todos')
        .then(res => res.json())
        .then(data => {
            const list = document.getElementById('todo-list');
            list.innerHTML = '';
            data.forEach(todo => {
                list.appendChild(createTodoItem(todo));
            });
        });
}

// 3. 할 일 추가 (입력값을 서버에 POST)
function addTodo() {
    const input = document.getElementById('todo-input');
    const content = input.value.trim();
    if (!content) return;
    fetch('/api/todos', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({content, completed: false})
    })
    .then(() => {
        input.value = '';
        loadTodos();
    });
}

// 4. 할 일 항목 하나를 생성 (체크박스, 인라인 수정, 삭제 버튼 포함)
function createTodoItem(todo) {
    const li = document.createElement('li');
    li.className = 'list-group-item d-flex justify-content-between align-items-center';
    if (todo.completed) li.classList.add('completed');

    // 4-1. 완료 체크박스 (원형)
    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.className = 'form-check-input me-2';
    checkbox.checked = todo.completed;
    checkbox.style.borderRadius = '50%';
    checkbox.onclick = () => {
        fetch(`/api/todos/${todo.id}/toggle`, {method: 'PUT'})
            .then(loadTodos);
    };

    // 4-2. 할 일 내용 (더블클릭 시 인라인 수정)
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
                fetch(`/api/todos/${todo.id}`, {
                    method: 'PUT',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({content: newContent, completed: todo.completed})
                }).then(loadTodos);
            } else {
                li.replaceChild(span, input);
            }
        }
    };

    // 4-3. 삭제 버튼 (이모지 휴지통)
    const delBtn = document.createElement('button');
    delBtn.className = 'btn btn-sm btn-outline-danger ms-2';
    delBtn.textContent = '🗑️'; // 이모지 휴지통
    delBtn.title = '삭제';

    delBtn.onclick = () => {
        if (confirm('삭제하시겠습니까?')) {
            fetch(`/api/todos/${todo.id}`, {method: 'DELETE'})
                .then(loadTodos);
        }
    };

    li.append(checkbox, span, delBtn);
    return li;
}
