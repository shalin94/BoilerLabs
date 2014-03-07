# Fetch the newest code 
git fetch 

# Delete all files which are being added, so there 
# are no conflicts with untracked files 
for file in `git diff HEAD..origin/master --name-status | awk '/^A/ {print $2}'`
do
    rm -f -- "$file"
done

# Checkout all files which were locally modified
for file in `git diff --name-status | awk '/^[CDMRTUX]/ {print $2}'`
do
    git checkout -- "$file"
done

# Finally pull all the changes (you could merge as well e.g. 'merge origin/master') 
git pull