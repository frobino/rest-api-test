# rest-api-test python

This is a simple *client*, i.e. consumes data from an existing server.
Use the GET method of one of the github api.

Is suggested to use venv:

```
python3 -m venv apis
source apis/bin/activate
pip install flask flask-jsonpify flask-sqlalchemy flask-restful
pip freeze
pip install requirements.txt
```

The project structure should look something like this [1]:

```
README.rst
LICENSE
Makefile
setup.py
requirements.txt
<projectName>/__init__.py
<projectName>/__main__.py
<projectName>/core.py
<projectName>/helpers.py
docs/conf.py
docs/index.rst
tests/test_basic.py
tests/test_advanced.py
```

[1] https://docs.python-guide.org/writing/structure/