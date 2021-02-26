/**
 * @class Model
 *
 * Interface to the model containing data.
 * Can be created by YAML file, once an API is defined.
 */
class Model {
  constructor(modelApiLocation) {
    this.modelApiLocation = modelApiLocation;
  }

  /**
   * POST {todoText}
   * 
   * @param {*} todoText text for the Todo to add
   */
  addTodo(todoText){
    let xhr = new XMLHttpRequest();
    let resource = modelApiLocation;
    // Using sync call, to make sure model gets updated before getting the model back
    xhr.open("POST", resource, false);
    xhr.send(todoText)
    // TODO: handle response
  }

  /**
   * DELETE to crunchify/model/1
   * 
   * @param {*} id the unique id of the Todo to delete
   */
  deleteTodo(id){
    let xhr = new XMLHttpRequest();
    let resource = modelApiLocation + '/' + id;
    // Using sync call, to make sure model gets updated before getting the model back
    xhr.open("DELETE", resource, false);
    // xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
    // xhr.setRequestHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Accept-Version, Content-MD5, CSRF-Token, Content-Type");
    // xhr.setRequestHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    xhr.send()    
    // TODO: handle response
  }

  /**
   * POST {id, todoText}
   * 
   * @param {*} id unique id of the Todo to update
   * @param {*} todoText new text to be used when updating the Todo
   */
  editTodo(id, todoText){
    let xhr = new XMLHttpRequest();
    let resource = modelApiLocation + '/' + id;
    // Using sync call, to make sure model gets updated before getting the model back
    xhr.open("POST", resource, false);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify({"id": id, "text": todoText, "complete": false}))
    // TODO: handle response
  }

  /**
   * PATCH {id}
   * 
   * @param {*} id unique id of the Todo to be toggled
   */
  toggleTodo(id){
    let xhr = new XMLHttpRequest();
    let resource = modelApiLocation + '/' + id;
    // Using sync call, to make sure model gets updated before getting the model back
    xhr.open("PATCH", resource, false);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify({"op": "toggle"}))
    // TODO: handle response
  }

}

/**
 * @class View
 *
 * Visual representation of the model.
 */
class View {
  constructor() {
    this.app = this.getElement('#root')
    this.form = this.createElement('form')
    this.input = this.createElement('input')
    this.input.type = 'text'
    this.input.placeholder = 'Add todo'
    this.input.name = 'todo'
    this.submitButton = this.createElement('button')
    this.submitButton.textContent = 'Submit'
    this.form.append(this.input, this.submitButton)
    this.title = this.createElement('h1')
    this.title.textContent = 'Todos'
    this.todoList = this.createElement('ul', 'todo-list')
    this.app.append(this.title, this.form, this.todoList)

    this._temporaryTodoText = ''
    this._initLocalListeners()
  }

  get _todoText() {
    return this.input.value
  }

  _resetInput() {
    this.input.value = ''
  }

  createElement(tag, className) {
    const element = document.createElement(tag)

    if (className) element.classList.add(className)

    return element
  }

  getElement(selector) {
    const element = document.querySelector(selector)

    return element
  }

  displayTodos(todos) {
    // Delete all nodes
    while (this.todoList.firstChild) {
      this.todoList.removeChild(this.todoList.firstChild)
    }

    // Show default message
    if (todos.length === 0) {
      const p = this.createElement('p')
      p.textContent = 'Nothing to do! Add a task?'
      this.todoList.append(p)
    } else {
      // Create nodes
      todos.forEach(todo => {
        const li = this.createElement('li')
        li.id = todo.id

        const checkbox = this.createElement('input')
        checkbox.type = 'checkbox'
        checkbox.checked = todo.complete

        const span = this.createElement('span')
        span.contentEditable = true
        span.classList.add('editable')

        if (todo.complete) {
          const strike = this.createElement('s')
          strike.textContent = todo.text
          span.append(strike)
        } else {
          span.textContent = todo.text
        }

        const deleteButton = this.createElement('button', 'delete')
        deleteButton.textContent = 'Delete'
        li.append(checkbox, span, deleteButton)

        // Append nodes
        this.todoList.append(li)
      })
    }

    // Debugging
    console.log(todos)
  }

  _initLocalListeners() {
    this.todoList.addEventListener('input', event => {
      if (event.target.className === 'editable') {
        this._temporaryTodoText = event.target.innerText
      }
    })
  }

  bindAddTodo(handler) {
    this.form.addEventListener('submit', event => {
      event.preventDefault()

      if (this._todoText) {
        handler(this._todoText)
        this._resetInput()
      }
    })
  }

  bindDeleteTodo(handler) {
    this.todoList.addEventListener('click', event => {
      // frobino: delete uses the id stored in html, this is GOOD to eliminate model
      if (event.target.className === 'delete') {
        const id = parseInt(event.target.parentElement.id)

        handler(id)
      }
    })
  }

  bindEditTodo(handler) {
    this.todoList.addEventListener('focusout', event => {
      if (this._temporaryTodoText) {
        const id = parseInt(event.target.parentElement.id)

        handler(id, this._temporaryTodoText)
        this._temporaryTodoText = ''
      }
    })
  }

  bindToggleTodo(handler) {
    this.todoList.addEventListener('change', event => {
      if (event.target.type === 'checkbox') {
        const id = parseInt(event.target.parentElement.id)

        handler(id)
      }
    })
  }
}

/**
 * @class Controller
 *
 * Links the user input and the view output.
 *
 * @param view
 */
class Controller {
  constructor(model, view) {
    this.model = model
    this.view = view

    // Explicit this binding
    this.view.bindAddTodo(this.handleAddTodo)
    this.view.bindEditTodo(this.handleEditTodo)
    this.view.bindDeleteTodo(this.handleDeleteTodo)
    this.view.bindToggleTodo(this.handleToggleTodo)

    // Display initial todos
    this.onTodoListChanged()
  }

  // MODEL -> VIEW

  onTodoListChanged = () => {
    // frobino: GET, receive list of todos [{},{},...]
    //
    // original:
    // this.view.displayTodos(todos)

    var getJSON = function(url, view, callback) {

      var xhr = new XMLHttpRequest();
      xhr.open('GET', url, true);
      xhr.responseType = 'json';

      xhr.onload = function() {

          var status = xhr.status;

          if (status == 200) {
              callback(null, xhr.response, view);
          } else {
              callback(status);
          }
      };

      xhr.send();
    };

    getJSON('http://localhost:8080/jaxrs-test-app/crunchify/model', this.view,
      function(err, data, view) {
        if (err != null) {
            console.error(err);
        } else {
         // Debug
         console.log('Output: ', data);
         view.displayTodos(data)
        }
      }
    );

  }

  // VIEW -> MODEL

  handleAddTodo = todoText => {

    this.model.addTodo(todoText)

    // TODO: this is just to trigger the onTodoListChanged method (making a GET of the whole model).
    // Replace with proper logic (e.g. call directly onTodoListChanged) and remove model.
    // this.model.addTodo(todoText)
    this.onTodoListChanged()
  }

  handleEditTodo = (id, todoText) => {

    this.model.editTodo(id, todoText)

    // TODO: this is just to trigger the onTodoListChanged method (making a GET of the whole model).
    // Replace with proper logic (e.g. call directly onTodoListChanged) and remove model.
    // this.model.editTodo(id, todoText)
    this.onTodoListChanged()
  }

  handleDeleteTodo = id => {

    this.model.deleteTodo(id)

    // TODO: this is just to trigger the onTodoListChanged method (making a GET of the whole model).
    // Replace with proper logic (e.g. call directly onTodoListChanged) and remove model.
    // this.model.deleteTodo(id)
    this.onTodoListChanged()
  }

  handleToggleTodo = id => {

    this.model.toggleTodo(id)

    // TODO: this is just to trigger the onTodoListChanged method (making a GET of the whole model).
    // Replace with proper logic (e.g. call directly onTodoListChanged) and remove model.
    // this.model.toggleTodo(id)
    this.onTodoListChanged()
  }
}

const modelApiLocation = "http://localhost:8080/jaxrs-test-app/crunchify/model"
const app = new Controller(new Model(modelApiLocation), new View())
