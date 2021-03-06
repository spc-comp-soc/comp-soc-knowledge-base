"""
In python we have special parameters named *args and **kwargs. "kw" means keyword.
If we do not know the amount or even the key of the arguments, we an use the above.
"""


# **kwargs(Keyword Arguments)
def student(normal_arg, **info):  # Double * is needed for kwargs.
    print(normal_arg)
    for key, value in info.items():  # Kwargs converts pairs of keywords and values into a dictionary.
        print(f"Key: {key}\n"
              f"Value: {value}")


student("This is a normal arg.", name="Charles", gender="Male", age=15)
# More than one kwargs can be passed, keywords are required.


# *args
def mom(*fruits):  # Single * is needed for args
    for item in fruits:  # Args transform multiple arguments into a tuple.
        print(f"My mom bought {item} in the market today.")


mom("apple", "orange", "pear", "mango")
# Again, multiple arguments can be passed in using *args.
