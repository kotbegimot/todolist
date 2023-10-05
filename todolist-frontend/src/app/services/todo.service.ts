import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Todo } from '../common/todo';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  private baseUrl = 'http://localhost:8080/api/v1/todos';

  constructor(private httpClient: HttpClient) { }

  getTodoList(): Observable<Todo[]> {
    return this.httpClient.get<GetResponse>(this.baseUrl).pipe(
      map(response => response.todos)
    );
  }
}

interface GetResponse {
    todos: Todo[];
}