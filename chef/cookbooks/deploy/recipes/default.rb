target_dir = node.deploy.target_dir
base_dir = node.deploy.base_dir
shared_dir = ::File.join(base_dir, "shared")
project = node.deploy.project
user = node.deploy.user

link ::File.join(target_dir, '.env') do
  to ::File.join(shared_dir, 'foreman_env')
end

foreman project do
  dir target_dir
  user user
end
