import { Component, OnInit } from '@angular/core';
import { TodoService } from 'src/app/services/todo.service';
import { Todo } from 'src/app/common/todo';

@Component({
  selector: 'app-todo-list',
  templateUrl: './todo-list-table.component.html',
  // templateUrl: './todo-list.component.html',
  styleUrls: ['./todo-list.component.css']
})
export class TodoListComponent implements OnInit {

  todos: Todo[] = [];
  
  constructor(private todoService: TodoService) { }

  ngOnInit() {
    this.listTodos();
  }

  listTodos() {
    this.todoService.getTodoList().subscribe(
      data => {
        this.todos = data;
      }
    )
  }

}
