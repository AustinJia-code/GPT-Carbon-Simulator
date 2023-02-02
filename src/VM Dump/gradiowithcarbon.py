from datetime import datetime
import pytz
from transformers import pipeline
#from codecarbon import EmissionsTracker
from io import StringIO
import sys
#tracker = EmissionsTracker()

title = "GPT Models Demonstration"
description = "This demo shows how various GPT models answer a prompt question."
article="The following shows the carbon emission of GPT models when answering questions."

cpu_util = 50

savePath = '/home/azureuser/Logs/'
outputFile = datetime.today().strftime('%Y-%m-%d')
fullPath = os.path.join(savePath, outputFile+".txt")
f = open(fullPath, "a")

examples = [
    ["gpt2", "What is a computer?"],
    ["gptj13B", 'What is a computer?'],
    ["gpt125M", 'What is a computer?'],
]

gpt2 = pipeline('text-generation', model='gpt2-large')
gpt2 = gr.Interface.load("huggingface/gpt2-large")
gptj13B = pipeline('text-generation', model='EleutherAI/gpt-neo-1.3B')
gptj13B = gr.Interface.load("huggingface/EleutherAI/gpt-neo-1.3B")
gptneo125M = pipeline('text-generation', model='EleutherAI/gpt-neo-125M')
gptneo125M = gr.Interface.load("huggingface/EleutherAI/gpt-neo-125M")

def fn(model_choice, input, MOER):
    start_time = time.time()
    output = ""
    #tracker = EmissionsTracker()
    #tracker.start()
    if model_choice=="gpt2":
        output = gpt2(input)
    elif model_choice=="gpt-j-1.3B":
        output = gptj13B(input)
    elif model_choice=="gpt-neo-125M":
        output = gptneo125M(input)
    exe_time = time.time()-start_time
    power = round(1.37*cpu_util + 41.32, 3)
    energy = power * exe_time / 1000 / 3600 #kilowatt hours
    carbon = energy / 1000 * float(MOER) * 453.592 #grams carbon output
    #temp_out = StringIO()
    #sys.stdout = temp_out
    #carbon_output = str(tracker.stop())
    #emissions: float = tracker.stop()
    #carbon_output = temp_out.getvalue()
    formattedOutput = "[" + datetime.now(pytz.timezone('US/Central')).strftime('%H:%M:%S') + "] - " + model_choice + " - " + str(exe_time) + " seconds\n" + input + "\n" + output
    #f.write(str(emissions) + "\n")
    f.write(formattedOutput)
    f.write("\n-----------------------------------------------------------------------\n")
    return (output, str(energy), str(carbon))

gr.Interface(fn, [gr.inputs.Dropdown(["gpt2", "gpt-j-1.3B", "gpt-neo-125M"]), "text", gr.components.Textbox(label="MOER")], outputs=[gr.components.Textbox(label="Output"), gr.components.Textbox(label="Energy"), gr.components.Textbox(label="Carbon")], examples=examples, title=title, description=description, article=article).launch(debug=True, share=True)