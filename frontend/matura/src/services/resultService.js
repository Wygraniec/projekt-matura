import axios from "axios";
import {User} from "./userService.js";

const API = `${import.meta.env.VITE_API_URL}/v1`;

export class Result {
    constructor(id, submissionId, subtaskNumber, description, score, createdAt) {
        this.id = id;
        this.submissionId = submissionId;
        this.subtaskNumber = subtaskNumber;
        this.description = description;
        this.score = score;
        this.createdAt = new Date(createdAt);
    }

    static async getBySubmissionId(submissionId) {
        const response = await axios.get(
            `${API}/submissions/${submissionId}/results`,
            User.fromLocalStorage().getAuthHeader()
        )

        return response.data
            .map(
                data => new Result(
                    data['id'],
                    data['submissionId'],
                    data['subtaskNumber'],
                    data['description'],
                    data['score'],
                    data['createdAt']
                )
            );
    }
}
