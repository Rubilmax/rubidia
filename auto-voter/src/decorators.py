from bs4 import BeautifulSoup

import time
from colored import fg


def proxies_getter(proxy_func):
    def decorator():
        print(fg(3), "Getting proxies...")

        proxies = proxy_func()
        print(fg(3), f"Got {len(proxies)} proxies!")

        return proxies
    return decorator


def vote_wrapper(name, url, message):
    def decorator(vote_func):
        def vote(driver):
            try:
                driver.get(url)

                time.sleep(4)

                vote_func(driver)
                content = BeautifulSoup(driver.page_source, "html.parser").text

                if message not in content:
                    print(fg(3), name, fg(2), "Success!")
                    return True
                else:
                    print(fg(3), name, fg(1), "Unsuccessful...")
            except Exception as ex:
                print(fg(3), name, fg(1), "Error:", ex)

            return False
        return vote
    return decorator
