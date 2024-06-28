import axios from 'axios'

const API = `${import.meta.env.VITE_API_URL}/v1`;

export class User {
    constructor(token, id, email, username, role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    static fromLocalStorage() {
        const storedUserJson = localStorage.getItem('user');

        if (!storedUserJson)
            return null;

        let data = JSON.parse(storedUserJson);

        return new User(data.token, data.id, data.email, data.username, data.role)
    }

    saveToLocalStorage() {
        localStorage.setItem('user', JSON.stringify(this))
    }

    getAuthHeader = (config = {}) => ({
        ...config,
        headers: {
            Authorization: `Bearer ${this.token}`
        }
    })

    async validate() {
        var userData = null;

        try {
            await axios
                .get(`${API}/users/me`, this.getAuthHeader())
                .then(res => userData = res.data)
        } catch (e) {
            // An error has occurred, so probably the API rejected the request
            // Therefore something was wrong with the token and user is invalid
            logout()
        }

        if(userData === null || this.id !== userData['id'] || this.role !== userData['role'])
            logout()

        return true;
    }

    static fromApiResponse(data) {
        return new User(
            data['token'],
            data['userDto']['id'],
            data['userDto']['email'],
            data['userDto']['username'],
            data['userDto']['role']
        )
    }
}

export const login = async (username, password) => {
    let response = await axios
        .post(`${API}/auth/login`, {
            username: username,
            password: password
        })
        .catch(e => {
            console.log('Error during login');
            throw e;
        });

    User.fromApiResponse(response.data).saveToLocalStorage();
}

export const logout = () => {
    localStorage.removeItem('user');
    window.location.href = '/login';
}
