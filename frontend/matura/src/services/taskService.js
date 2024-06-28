import axios from "axios";
import {User} from "./userService.js";
import {Template} from "./templateService.js";

const API = `${import.meta.env.VITE_API_URL}/v1`;

export class Task {
    constructor(id, templateId, state, createdAt, createdBy, numberOfSubtasks) {
        this.id = id
        this.templateId = templateId
        this.state = state
        this.createdAt = createdAt
        this.createdBy = createdBy
        this.numberOfSubtasks = numberOfSubtasks
    }

    async getTemplate() {
        return await Template.findById(this.templateId)
    }

    static async findById(id) {
        let endpoint = `${API}/tasks/${id}`
        let data = (await axios.get(endpoint, User.fromLocalStorage().getAuthHeader())).data

        return new Task(
            data['id'],
            data['templateId'],
            data['state'],
            data['createdAt'],
            data['createdBy'],
            data['numberOfSubtasks']
        )
    }

    async getFile() {
        let endpoint = `${API}/tasks/${this.id}/file`
        const response = await axios.get(endpoint, User.fromLocalStorage().getAuthHeader({ responseType: 'text' }));

        return response.data;
    }

    async saveFile(fileContents) {
        let endpoint = `${API}/tasks/${this.id}/file`

        const blob = new Blob([fileContents], { type: 'text/plain' })
        const formData = new FormData();
        formData.append('file', blob, 'task.py')

        const response = await axios.post(
            endpoint,
            formData,
            User.fromLocalStorage().getAuthHeader({
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }))

        console.log(response.data)
    }
}

export class TaskPage {
    constructor(tasks, currentPage, totalPages, totalElements) {
        this.tasks = tasks
        this.currentPage = currentPage
        this.totalPages = totalPages
        this.totalElements = totalElements
    }
}

export const getTasks = async (page = 0, pageSize = 10, userId = 0, states = null) => {
    let endpoint = `${API}/users/${userId}/tasks/byState?page=${page}&size=${pageSize}`

    states.forEach(state => endpoint += `&taskStates=${state}`)


    let request = await axios.get(endpoint, User.fromLocalStorage().getAuthHeader())

    return new TaskPage(
        request.data['tasks'].map(data => new Task(data['id'], data['templateId'], data['state'], new Date(data['createdAt']), request.data['createdBy'], request.data['numberOfSubtasks'])),
        request.data['currentPage'],
        request.data['totalPages'],
        request.data['totalElements'],
    )
}
