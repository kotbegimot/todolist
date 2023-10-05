USE todo_db;
CREATE TABLE IF NOT EXISTS todos (
  todo_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) DEFAULT NULL
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS tasks (
	task_id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255) DEFAULT NULL,
	todo_id INT, 
	FOREIGN KEY (todo_id) REFERENCES todos(todo_id) ON DELETE SET NULL
) ENGINE=InnoDB;;
INSERT INTO todos(name, description) VALUES
('Clean bathroom', 'Home duty'),
('Make lunch', 'Pasta with cevapcici'),
('Throw out paper box', 'Home duty');
INSERT INTO tasks (name, description, todo_id) VALUES
  ('Clean wash basin', 'Weekly', 1),
  ('Clean toilet', 'Weekly', 1),
  ('Clean shower cabin', 'Bi-weekly', 1),
  ('Boil pasta', '10 min', 2),
  ('Warm up cevapcici', '8 min', 2),
  ('Make salad', 'Rucola and tomatoes', 2),
  ('Check paper bin capacity', 'Bin is empty on Monday', 3),
  ('Throw out box', 'If container is not full', 3);