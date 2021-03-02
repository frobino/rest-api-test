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

  bindTodoListChanged(callback) {
    this.onTodoListChanged = callback
  }

  /**
   * Helper to enable async requests.
   * Include logic to specify what to do once a
   * request is considered successful / accepted.
   * 
   * @param {*} xhr opened XMLHttpRequest object
   * @param {*} acceptedStatus request response that can
   * be considered acceptable to execute callback
   */
  _setupRequest(xhr, acceptedStatus){
    let callback = this.getTodosAndExec
    let callbackArg = this.onTodoListChanged
    xhr.onload = function() {
      var status = xhr.status;
      if (status == acceptedStatus) {
        // Here we specify what to do everytime a request is successful
        callback((todos) => callbackArg(todos))
      } else {
        // TODO: improve error handling
        console.error(status);
      }
    };
  }

  /**
   * POST {todoText}
   * 
   * @param {*} todoText text for the Todo to add
   */
  addTodo(todoText){
    let xhr = new XMLHttpRequest();
    let resource = modelApiLocation;
    xhr.open("POST", resource, true);
    this._setupRequest(xhr, 201)
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
    xhr.open("DELETE", resource, true);
    // xhr.setRequestHeader("Access-Control-Allow-Origin", "*");
    // xhr.setRequestHeader("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Accept-Version, Content-MD5, CSRF-Token, Content-Type");
    // xhr.setRequestHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    this._setupRequest(xhr, 204)
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
    xhr.open("POST", resource, true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    this._setupRequest(xhr, 200)
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
    xhr.open("PATCH", resource, true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    this._setupRequest(xhr, 200)
    xhr.send(JSON.stringify({"op": "toggle"}))
    // TODO: handle response
  }

  /**
   * GET and execute a function on the result data returned
   * 
   * @param {*} callback - unary function to be executed
   * with the GET call result. The function should
   * get the JSON representation of all todos as parameter
   * and act on it.
   * The returned JSON representation is 
   * [{"id":1,"text":"t1","complete":false}, ...]
   */
  getTodosAndExec(callback) {

    var xhr = new XMLHttpRequest();
    let resource = modelApiLocation;
    xhr.open('GET', resource, true);
    xhr.responseType = 'json';

    xhr.onload = function() {
      var status = xhr.status;
      if (status == 200) {
        callback(xhr.response);
      } else {
        // TODO: improve error handling
        console.error(status);
      }
    };

    xhr.send();
    // TODO: handle response
  };

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

  /**
   * Configure the view to execute an handler when a
   * "Submit" type of event is emitted (user clicks on
   * "add todo" button - see constructor).
   * 
   * @param {*} handler - unary function to be executed
   * when the submit event is emitted. The function should
   * get the todo text as parameter and act on it.
   */
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
    /*
     * NOTE: we use mainly callbacks.
     * How callback vs promises work:
     * https://stackoverflow.com/questions/38829610/how-i-can-return-xmlhttprequest-response-from-function
     */
    this.view.bindAddTodo(this.handleAddTodo)
    this.view.bindEditTodo(this.handleEditTodo)
    this.view.bindDeleteTodo(this.handleDeleteTodo)
    this.view.bindToggleTodo(this.handleToggleTodo)

    this.model.bindTodoListChanged(this.onTodoListChanged)

    // Display initial todos
    /*
     * NOTE: the following syntax does not work due to meaning of "this":
     * https://stackoverflow.com/questions/20279484/how-to-access-the-correct-this-inside-a-callback
     * 
     * this.model.getTodosAndExec(this.view.displayTodos)
     */
    // this.model.getTodosAndExec((data) => this.view.displayTodos(data))
    this.model.getTodosAndExec((data) => this.onTodoListChanged(data))
  }

  /**
   * Handler (callback) function that should be executed
   * everytime the model todo list changes.
   * 
   * MODEL -> VIEW
   * Using the model.getTodosAndExec, data is retreived
   * from the model, and the data is forwarded to the
   * view.displayTodos to be displayed
   * 
   */
  onTodoListChanged = todos => {
    this.view.displayTodos(todos)
  }

  /**
   * Handler (callback) function that should be executed
   * everytime the user clicks on the "add todo" button.
   * 
   * VIEW -> MODEL
   * It sends the todo text to the model
   * 
   * @param {*} todoText text to be added
   */
  handleAddTodo = todoText => {
    this.model.addTodo(todoText)
  }

  handleEditTodo = (id, todoText) => {
    this.model.editTodo(id, todoText)
  }

  handleDeleteTodo = id => {
    this.model.deleteTodo(id)
  }

  handleToggleTodo = id => {
    this.model.toggleTodo(id)
  }
}

const modelApiLocation = "http://localhost:8080/jaxrs-test-app/crunchify/model"
const app = new Controller(new Model(modelApiLocation), new View())
