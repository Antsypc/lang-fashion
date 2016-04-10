USE github;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS repo;

CREATE TABLE user (
  id           INT PRIMARY KEY,
  login        VARCHAR(40) NOT NULL,  # login username
  type         VARCHAR(20),           # User?
  site_admin   TINYINT,               # 0 means false
  name         VARCHAR(255),
  company      VARCHAR(255),
  location     VARCHAR(255),
  email        VARCHAR(255),
  public_repos SMALLINT,              # public number
  public_gists SMALLINT,
  followers    INT,
  following    INT,
  created_at   INT,                   # first time sign in github
  updated_at   INT                    # lateast update time
)CHARSET = utf8;

CREATE TABLE repo (
  id               INT PRIMARY KEY,
  name             VARCHAR(255) NOT NULL,
  owner_id         INT          NOT NULL,
  owner_login      VARCHAR(40)  NOT NULL,
  language         VARCHAR(20),
  fork             TINYINT,           # 1 means forked from others
  created_at       INT,
  updated_at       INT,
  pushed_at        INT,
  size             INT,
  stargazers_count INT,
  watchers_count   INT,
  forks_count      INT
)CHARSET = utf8;
