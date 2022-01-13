#!/bin/sh

img_name=schedule-backend
packed_img_name=${img_name}.tar

buildDockerImage() {
  docker image rm ${img_name}
  docker build -t ${img_name} .
}

packBackend() {
  echo packing image...
  docker save -o ${packed_img_name} ${img_name}
  echo image packed
}

clearDockerImages() {
  docker-compose stop &&
    docker-compose down &&
    docker image rm ${img_name}
  docker image prune -f
}

deployDockerImages() {
  docker load -i ${packed_img_name} &&
    docker-compose up -d
}

case "$1" in
"local")
  clearDockerImages &&
    buildDockerImage &&
    docker-compose up -d &&
     exit 0
  ;;
"stage")
  clearDockerImages &&
    buildApp &&
    buildDockerImage &&
    packBackend &&
     exit 0
  ;;
*)
  echo expected stage or local, but was $1
  exit 1
  ;;
esac