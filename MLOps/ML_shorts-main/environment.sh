
# Get the operating system type
os_type=$(uname)

if [[ "$os_type" == "Darwin" ]]; then
    # If it's macOS
    echo "macOS"
    rm -r python3_environment
    python3 -m venv python3_environment
    source python3_environment/bin/activate
    pip install ipykernel
    python -m ipykernel install --user --name=python3_environment --display-name="Python (python3_environment)"
    pip install --upgrade pip
    # pip install -r requirements.txt

elif [[ "$os_type" == "Linux" ]]; then
    # If it's Linux
    echo "Linux"
    rm -r python3_environment
    python3 -m venv python3_environment
    source python3_environment/bin/activate
    pip install ipykernel
    python -m ipykernel install --user --name=python3_environment --display-name="Python (python3_environment)"
    pip install --upgrade pip
    pip install -r requirements.txt
else
    echo "Unknown operating system"
fi