import axios from "axios";
import {User} from "./userService.js";

const API = `/api`;

export class Template {
    constructor(id, language, statement, source, createdAt, numberOfSubtasks) {
        this.id = id
        this.language = language
        this.statement = statement
        this.source = source
        this.createdAt = createdAt
        this.numberOfSubtasks = numberOfSubtasks
    }

    static async findById(id) {
        let endpoint = `${API}/templates/${id}`
        let data = (await axios.get(endpoint, User.fromLocalStorage().getAuthHeader())).data

        return new Template(
            data['id'],
            data['taskLanguage'],
            data['statement'],
            data['source'],
            data['createdAt'],
            data['numberOfSubtasks'],
        )
    }
}

export class TemplatePage {
    constructor(templates, currentPage, totalPages, totalElements) {
        this.templates = templates
        this.currentPage = currentPage
        this.totalPages = totalPages
        this.totalElements = totalElements
    }
}

export const getTemplates = async (page = 0, pageSize = 10, language = '', source = '') => {
    let endpoint = `${API}/templates?page=${page}&size=${pageSize}`

    source = `*${source.replaceAll(' ', '*').replaceAll('.', '*')}*`

    if(language)
        endpoint += `&taskLanguage=${language}`

    if(source.replaceAll('*', ''))
        endpoint += `&source=${source.replaceAll('*', '%25')}`

    let request = await axios.get(endpoint, User.fromLocalStorage().getAuthHeader())

    return new TemplatePage(
        request.data['templates'].map(data => new Template(data['id'], data['taskLanguage'], data['statement'], data['source'], new Date(data['createdAt']), data['numberOfSubtasks'])),
        request.data['currentPage'],
        request.data['totalPages'],
        request.data['totalElements']
    )
}

export const getAvailableLanguages = async () => {
    let request = await axios.get(`${API}/templates/languages`, User.fromLocalStorage().getAuthHeader())
    return Object.values(request.data).sort()
}
