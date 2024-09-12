import axios from "axios";
import {User} from "./userService.js";

const API = `/api`;

export class Result {
    constructor(id, submissionId, subtaskNumber, description, score, createdAt) {
        this.id = id;
        this.submissionId = submissionId;
        this.subtaskNumber = subtaskNumber;
        this.description = description;
        this.score = score;
        this.createdAt = new Date(createdAt);
    }

    getParsedDescription() {
        return this.description
            .split('\n')
            .slice(0, -1)
            .map(el => el.split(';'))
            .map(test => ({
                submittedAt: new Date(test[0]),
                testName: test[1],
                passed: test[2] === 'True',
                time: parseFloat(test[3])
            }));
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
