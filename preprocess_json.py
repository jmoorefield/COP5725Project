import ijson
import gzip
import json 

# parse function provided by https://cseweb.ucsd.edu/~jmcauley/datasets/amazon/links.html
def parse(path):
  g = gzip.open(path, 'r')
  for l in g:
    yield json.dumps(eval(l))

with open("/Users/jess/Desktop/metadata_true.json", 'w') as f :
    f.write("[")
    counter = 0
    for l in parse("/Users/jess/Downloads/metadata.json.gz"):
        if counter < 21500 :
            f.write(l + ',\n')
            counter += 1
            print(counter)
        else: 
            break
    f.write("]")

simplified = "/Users/jess/Desktop/metadata_simplified.json"
with open('/Users/jess/Desktop/metadata_true.json', 'r') as file:
    parser = ijson.items(file, 'item')
    blob = []
    counter = 0 

    # number of leaves in the original Amazon dataset 
    for item in parser:
        if counter < 21428 :
            entry = {
            "title": "",
            "categories": ""
            }
            if "categories" in item.keys() and "title" in item.keys() :
                entry["title"] = item["title"]
                entry["categories"] = item["categories"]
                blob.append(entry)

                print(counter)
                counter += 1
        else :
            break

    json_string = json.dumps(blob, indent=4)
    with open(simplified, 'w') as t :
        t.write(json_string)
 
   




