import gradio as gr
import time
import os.path
from datetime import datetime
from io import StringIO
import sys
import requests
import json


def jprint(obj):
    text = ""
    for x in obj['data']:
            text += x
    return text

title = "GPT Interface"
description = "This interface uses APIs to connect to different VMs"
article="The following shows the carbon emission of GPT models when answering questions."

VirginiaURL = 'https://8b2ceb48d74ce910.gradio.app/api/predict'

examples = [
    ["gpt2", 'What is a computer?', "Virginia"],
    ["gptj13B", 'What is a computer?', "Virginia"],
    ["gpt125M", 'What is a computer?', "Virginia"]
]

def fn(model_choice, input, location):
    start_time = time.time()

    if location == "Virginia":
        url = VirginiaURL

    data = {'data': [model_choice, input]}
    response = requests.post(url, json=data)
    genOutput = jprint(response.json())
    strRuntime = str(time.time()-start_time)
    formattedOutput = location + " " +  strRuntime + "\n" + genOutput

    return formattedOutput

gr.Interface(fn, [gr.inputs.Dropdown(["gpt2", "gpt-j-1.3B", "gpt-neo-125M"]), "text", gr.inputs.Dropdown(["Virginia"])], "text", examples=examples, title=title, description=description, article=article).launch(debug=True, share=True)