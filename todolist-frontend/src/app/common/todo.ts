import { Task } from './task'

export class Todo {

    constructor(public id: number,
                public name: string,
                public description: string,
                public tasks: Task[]
        ) {
    }
}