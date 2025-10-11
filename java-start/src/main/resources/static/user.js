// JWT 토큰 저장/가져오기/삭제
function setToken(token) {
    localStorage.setItem('jwtToken', token);
}
function setRefreshToken(token) {
    localStorage.setItem('refreshToken', token);
}
function getToken() {
    return localStorage.getItem('jwtToken');
}
function getRefreshToken() {
    return localStorage.getItem('refreshToken');
}
function removeToken() {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('username');
}

// 회원가입
function register(username, password) {
    fetch('/api/users/register', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, password})
    })
    .then(res => res.ok ? res.json() : res.json().then(err => { throw new Error(err.message); }))
    .then(user => {
        alert('회원가입 성공! 로그인 해주세요.');
    })
    .catch(err => alert(err.message));
}

// 로그인 (콜백으로 로그인 성공 시 사용자 정보 전달)
function login(username, password, onSuccess) {
    fetch('/api/users/login', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({username, password})
    })
    .then(res => {
        if (!res.ok) return res.json().then(err => { throw new Error(err.message); });
        const accessToken = res.headers.get('Authorization')?.replace('Bearer ', '');
        const refreshToken = res.headers.get('Refresh-Token');
        setToken(accessToken);
        setRefreshToken(refreshToken);
        return res.json();
    })
    .then(user => {
        localStorage.setItem('username', user.username);
        if (onSuccess) onSuccess(user);
    })
    .catch(err => alert(err.message));
}

// 로그아웃
function logout() {
    fetch('/api/users/logout', { method: 'POST' })
        .then(() => {
            removeToken();
            alert('로그아웃 되었습니다.');
        });
}

// AccessToken 재발급 함수
function refreshAccessToken(onSuccess, onError) {
    const refreshToken = getRefreshToken();
    if (!refreshToken) {
        if (onError) onError("RefreshToken이 없습니다.");
        return;
    }
    fetch('/api/users/refresh', {
        method: 'POST',
        headers: {
            'Refresh-Token': refreshToken
        }
    })
    .then(res => {
        if (!res.ok) return res.text().then(msg => { throw new Error(msg); });
        const newAccessToken = res.headers.get('Authorization')?.replace('Bearer ', '');
        setToken(newAccessToken);
        if (onSuccess) onSuccess(newAccessToken);
    })
    .catch(err => {
        removeToken();
        alert('로그인이 만료되었습니다. 다시 로그인 해주세요.');
        if (onError) onError(err.message);
    });
}

// 인증이 필요한 API 요청 예시
function fetchWithAuth(url, options = {}) {
    const token = getToken();
    return fetch(url, {
        ...options,
        headers: {
            ...options.headers,
            'Authorization': token ? 'Bearer ' + token : ''
        }
    }).then(res => {
        if (res.status === 401 && getRefreshToken()) {
            // AccessToken 만료 시 RefreshToken으로 재발급 시도
            return new Promise((resolve, reject) => {
                refreshAccessToken(() => {
                    // 재발급 성공 시 원래 요청 재시도
                    fetchWithAuth(url, options).then(resolve).catch(reject);
                }, reject);
            });
        }
        return res;
    });
}
