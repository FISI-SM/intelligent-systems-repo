
# Get the operating system type
os_type=$(uname)

if [[ "$os_type" == "Darwin" ]]; then
    # If it's macOS
    echo "macOS"
    rm -r venv_python3
    python3 -m venv venv_python3
    source venv_python3/bin/activate
    pip install ipykernel
    python -m ipykernel install --user --name=venv_python3 --display-name="Python (venv_python3)"
    pip install --upgrade pip
    pip install -r requirements.txt

elif [[ "$os_type" == "Linux" ]]; then
    # If it's Linux
    echo "Linux"
    rm -r venv_python3
    python3 -m venv venv_python3
    source venv_python3/bin/activate
    pip install ipykernel
    python -m ipykernel install --user --name=venv_python3 --display-name="Python (venv_python3)"
    pip install --upgrade pip
    pip install -r requirements.txt
else
    echo "Unknown operating system"
fi