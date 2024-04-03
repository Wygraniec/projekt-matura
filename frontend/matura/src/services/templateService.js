import axios from "axios";
import {User} from "./userService.js";

const API = `${import.meta.env.VITE_API_URL}/v1`;

export class Template {
    constructor(id, language, source) {
        this.id = id
        this.language = language
        this.source = source
    }
}

export const getTemplates = async (page = 1, pageSize = 10) => {

    let request = await axios.get(`${API}/templates?page=${page}&size=${pageSize}`, User.fromLocalStorage().getAuthHeader())
    console.log(request.data)
}
