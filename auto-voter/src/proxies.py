from decorators import proxies_getter
from http_request_randomizer.requests.proxy.requestProxy import RequestProxy


@proxies_getter
def get_from_file():
    proxies = []

    with open("proxy-list.txt", "r") as txt_file:
        proxies = [proxy.strip("\n") for proxy in txt_file.readlines()]

    return proxies


@proxies_getter
def get_online():
    proxies = []

    req_proxy = RequestProxy()
    proxies = [proxy.get_address() for proxy in req_proxy.get_proxy_list()]

    return proxies
