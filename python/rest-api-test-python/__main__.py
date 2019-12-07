import requests

def _url(endpoint):
    return 'https://api.github.com' + endpoint

# <method> <endpoint>
# GET      /search/users
payload = {'q':'frobino'}
resp = requests.get(_url('/search/users'), params=payload)
if resp.status_code != 200:
    # This means something went wrong.
    # raise ApiError('GET /tasks/ {}'.format(resp.status_code))
    raise print(resp.status_code)

# following documentation at https://developer.github.com/v3/search/#search-users,
# the returned json oblect is:
# {
#   "total_count": 12,
#   "incomplete_results": false,
#   "items": [
#     {
#       "login": "mojombo",
#       ...
#       "html_url": "https://github.com/mojombo"
#       ...
#     }
#   ]
# }
if (resp.json()['total_count'] > 0):
    users = resp.json()['items']
    for user in users:
        # print user login name and url
        print('{} {}'.format(user['login'], user['html_url']))