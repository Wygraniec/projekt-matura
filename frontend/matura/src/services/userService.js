import axios from 'axios'

const API = import.meta.env.VITE_API_URL;

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
            throw new Error("There is no user saved locally")

        let data = JSON.parse(storedUserJson);

        return new User(data.token, data.id, data.email, data.username, data.role)
    }

    saveToLocalStorage() {
        localStorage.setItem('user', JSON.stringify(this))
    }

    // TBD: implement a validation method working properly (a better API call?)
    async validate() {
        let response = await axios
            .get(`${API}/v1/users/${this.id}`, {
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': this.token,
                }
            })
            .catch(() => {
                console.log('Verification unsuccessful');
            })
            .then(result => console.log(result))
    }

    static fromApiResponse(data) {
        return new User(
            data['token'],
            data['userDto']['id'],
            data['userDto']['email'],
            data['userDto']['username'],
            data['userDto']['role'])
    }
}

export const login = async (username, password) => {
    let response = await axios
        .post(`${API}/v1/auth/login`, {
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
